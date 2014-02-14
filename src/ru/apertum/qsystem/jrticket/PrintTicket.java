/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsystem.jrticket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import javax.print.PrintServiceLookup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import ru.apertum.qsystem.client.common.WelcomeParams;
import ru.apertum.qsystem.common.QLog;
import ru.apertum.qsystem.common.model.QCustomer;
import ru.apertum.qsystem.extra.IPrintTicket;
import ru.apertum.qsystem.server.model.ATreeModel;
import ru.apertum.qsystem.server.model.QAdvanceCustomer;

/**
 *
 * @author Evgeniy Egorov
 */
public class PrintTicket implements IPrintTicket {

    private final static String TICKET_TEMPLATE = "plugins/jrt_template/ticket.jasper";
    private final static String TICKET_ADV_TEMPLATE = "plugins/jrt_template/ticket_adv.jasper";
    final private HashMap<String, Object> parameters = new HashMap();

    private HashMap<String, Object> getParams(QCustomer customer, QAdvanceCustomer advCustomer, String caption) {

        parameters.put("caption", caption);
        parameters.put("promo_text", WelcomeParams.getInstance().promoText);
        parameters.put("bottom_text", WelcomeParams.getInstance().bottomText);
        if (customer != null) {
            parameters.put("customer_number", customer.getNumber());
            parameters.put("customer_prefix", customer.getPrefix());
            parameters.put("customer_priority", customer.getPriority().get());
            parameters.put("customer_input_data", customer.getInput_data());
            parameters.put("customer_id", customer.getId());
            parameters.put("service_description", customer.getService().getDescription());
            parameters.put("service_input_caption", customer.getService().getInput_caption());
            parameters.put("service_name", customer.getService().getName());
            parameters.put("service_pre_info_text", customer.getService().getPreInfoPrintText());
            parameters.put("service_ticket_text", customer.getService().getTicketText());
            parameters.put("service", customer.getService());
            parameters.put("customer", customer);
        }
        if (advCustomer != null) {
            parameters.put("adv_customer_input_data", advCustomer.getInputData());
            parameters.put("adv_customer_id", advCustomer.getId());
            parameters.put("adv_customer", advCustomer);
            parameters.put("adv_customer_time", advCustomer.getAdvanceTime());
            parameters.put("service_description", advCustomer.getService().getDescription());
            parameters.put("service_input_caption", advCustomer.getService().getInput_caption());
            parameters.put("service_name", advCustomer.getService().getName());
            parameters.put("service_pre_info_text", advCustomer.getService().getPreInfoPrintText());
            parameters.put("service_ticket_text", advCustomer.getService().getTicketText());
            parameters.put("service", advCustomer.getService());
        }
        return parameters;
    }

    private boolean print(QCustomer customer, QAdvanceCustomer advCustomer, String caption, String template) {
        final File f_temp = new File(template);
        final InputStream inStr;
        try {
            inStr = new FileInputStream(f_temp);
        } catch (FileNotFoundException ex) {
            QLog.l().logger().error("Не найден шаблон талона.", ex);
            return false;
        }

        final JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport(inStr, getParams(customer, advCustomer, caption)); //это используя уже откампиленный
        } catch (JRException ex) {
            QLog.l().logger().error("Не сформирован талон.", ex);
            return false;
        }
        final JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, PrintServiceLookup.lookupDefaultPrintService());
        //exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, PrintServiceLookup.lookupDefaultPrintService().getAttributes());

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        // We set the selected service and pass it as a paramenter 
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, WelcomeParams.getInstance().printAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        try {
            exporter.exportReport();
        } catch (JRException ex) {
            QLog.l().logger().error("Ошибка вывода на печать талона.", ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean printTicket(QCustomer customer, String caption) {
        return print(customer, null, caption, TICKET_TEMPLATE);
    }

    @Override
    public boolean printTicketAdvance(QAdvanceCustomer advCustomer, String caption) {
        return print(null, advCustomer, caption, TICKET_ADV_TEMPLATE);
    }

    @Override
    public String getDescription() {
        return "Плагин дизайна и печати талона. Data " + DATE_ + "; Version " + VERSION_ + "; UID " + UID_ + ";";
    }

    @Override
    public long getUID() {
        return 01l;
    }
    public final static String DATE = "date";
    public final static String VERSION = "version";
    public final static String UID = "UID";
    public static String DATE_ = "";
    public static String VERSION_ = "";
    public static String UID_ = "";

    static {
        final Properties settings = new Properties();
        //"/ru/apertum/qsystem/reports/web/"
        final InputStream inStream = new String().getClass().getResourceAsStream("/jrticket.properties");

        try {
            settings.load(inStream);
        } catch (IOException ex) {
        }
        DATE_ = settings.getProperty(DATE);
        VERSION_ = settings.getProperty(VERSION);
        UID_ = settings.getProperty(UID);
    }

    @Override
    public boolean printTicketComplex(QCustomer customer, ATreeModel tm) {
        return false;
    }

}
