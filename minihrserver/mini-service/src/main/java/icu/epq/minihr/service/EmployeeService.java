package icu.epq.minihr.service;


import icu.epq.minihr.mapper.EmployeeMapper;
import icu.epq.minihr.model.Employee;
import icu.epq.minihr.model.MailConstants;
import icu.epq.minihr.model.MailSendLog;
import icu.epq.minihr.model.RespPageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author EPQ
 */
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MailSendLogService mailSendLogService;

    public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    public RespPageBean getEmployeeByPage(Integer page, Integer size, Employee employee, Date[] dates) {

        if (page != null && size != null) {
            page = (page - 1) * size;
        }

        List<Employee> data = employeeMapper.getEmployeeByPage(page, size, employee, dates);
        Long total = employeeMapper.getTotal(employee, dates);

        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setTotal(total);
        respPageBean.setData(data);

        return respPageBean;
    }

    public Integer addEmployee(Employee employee) {
        long beginDate = employee.getBeginContract().getTime();
        long endDate = employee.getEndContract().getTime();
        long date = endDate - beginDate;
        double days = (double) (date / (1000 * 60 * 60 * 24));
        double year = Double.parseDouble(String.format("%.2f", days / 365));
        employee.setContractTerm(year);
        employee.setWorkState("在职");
        int result = employeeMapper.insertSelective(employee);
        if (result == 1) {
            Employee emp = employeeMapper.getEmployeeById(employee.getId());
            //生成消息的唯一id
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setCreateTime(new Date());
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailSendLog.setEmpId(emp.getId());
            mailSendLog.setTryTime(new Date(System.currentTimeMillis() + 1000 * 60 * MailConstants.MSG_TIMEOUT));
            mailSendLogService.insert(mailSendLog);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, emp, new CorrelationData(msgId));
        }
        return result;
    }

    public synchronized Integer getMaxWorkID() {
        return employeeMapper.getMaxWorkID();
    }

    public Integer deleteEmployeeById(Integer id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    public Integer updateEmployee(Employee employee) {
        return employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public Employee getEmployeeById(Integer empId) {
        return employeeMapper.getEmployeeById(empId);
    }
}
