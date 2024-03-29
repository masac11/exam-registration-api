package com.exam.registration.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.exam.registration.config.AlipayConfig;
import com.exam.registration.model.*;
import com.exam.registration.service.*;
import com.exam.registration.util.MsgUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author yhf
 * @classname AlipayController
 **/
@RequestMapping("/alipay")
@RestController
public class AlipayController {

    @Autowired
    private AlipayService alipayService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private OrderService orderService;

    /**
     * web 订单支付
     */
    @RequestMapping(path = "/getWebPay", method = RequestMethod.POST)
    public String getPagePay(@RequestBody Map<String, Object> map,
                             HttpServletRequest request) throws Exception{
        Student student = studentService
                .getStudentByPrimaryKey(Long.valueOf((String) request.getAttribute("studentId")));
        Long majorId = Long.valueOf(String.valueOf(map.get("majorId")));
        Long siteId = Long.valueOf(String.valueOf(map.get("siteId")));
        Long orderId = Long.valueOf(String.valueOf(map.get("orderId")));
        Order order = orderService.getOrderByPrimaryKey(orderId);
        if (order.getIsPaid()) {
            return MsgUtils.fail("缴费单已支付，请刷新！");
        }
        Major major = majorService.getMajorByPrimaryKey(majorId);
        Site site = siteService.getSiteByPrimaryKey(siteId);
        BigDecimal fee = major.getFee();
        //生成订单
        String outTradeNo = null;
        if (StringUtils.areNotEmpty(order.getOrderNumber())) {// 之前有订单号，用之前的订单号
            outTradeNo = order.getOrderNumber();
        } else {// 之前未有订单号，新生成
            outTradeNo = site.getCode()
                    + major.getCode() + student.getId() + "-"
                    + UUID.randomUUID().toString().replace("-","");
        }
        String subject = site.getName() + "-" + major.getName();
        order.setOrderNumber(outTradeNo);
        order.setCost(fee);
        orderService.updateOrderByPrimaryKeySelective(order);
        String payForm = alipayService.webPagePay(outTradeNo, fee, subject);
        return MsgUtils.success(payForm);
    }

    @RequestMapping(path = "/notify", method = RequestMethod.POST)
    public void notify(HttpServletRequest request, String out_trade_no, String trade_no, String trade_status) throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            map.put(name, valueStr);
        }
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(map, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
        } catch (com.alipay.api.AlipayApiException e) {
        }
        if (signVerified) {
            //处理业务逻辑，更细订单状态等
            Order order = orderService.getOrderByOrderNumber(out_trade_no);
            BigDecimal trueCost = new BigDecimal(String.valueOf(map.get("total_amount")));
            if (Objects.isNull(order)) {
                throw new Exception("订单号不存在！");
            }
            if (trueCost.compareTo(order.getCost()) != 0) {
                System.out.println(trueCost.toString());
                System.out.println(order.getCost().toString());
                throw new Exception("金额错误！");
            }
            // 验证通过，修改订单的支付状态
            order.setIsPaid(true);
            orderService.updateOrderByPrimaryKeySelective(order);
            // 生成考号
            orderService.getExamNumber(order);
        } else {
            throw new Exception("签名验证错误");
        }
    }

    @RequestMapping(path = "/return", method = RequestMethod.GET)
    public String returnMsg() {
        return "支付成功！请返回界面刷新查看";
    }
}


