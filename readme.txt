ѕлагин дл€ создани€ произвольного дизайна талонов на основе шаблона JasperReports5.2.0.
“ребуетс€ верси€ QSystem1.4.2 или новее.
јналогичный плагин разработан сторонним разработчиком и опубликован тут http://qsystem.info/index.php/forum/6-%D0%9F%D0%BB%D0%B0%D0%B3%D0%B8%D0%BD%D1%8B-%D0%B2%D0%B7%D0%B0%D0%B8%D0%BC%D0%BE%D0%B4%D0%B5%D0%B9%D1%81%D1%82%D0%B2%D0%B8%D0%B5-%D1%81-%D0%B4%D1%80%D1%83%D0%B3%D0%B8%D0%BC%D0%B8-%D1%81%D0%B8%D1%81%D1%82%D0%B5%D0%BC%D0%B0%D0%BC%D0%B8/396-konstruktor-talonov
“ам хорошо описаны некоторые технические моменты работы и дезайна талонов. “ак же есть краткий мануал как работать с редактором iReport.

ƒл€ редактировани€ шаблонов лучше использовать программу iReport Designer. ќна бесплатна€.
Ќа работу плагина так же вли€ют параметра принтера из welcome.properties.
≈сли во врем€ работы плагина произойдет кака€ то ошибка, печать талона будет передана стандартному механизму QSystem, поэтому клиент без талона не останетс€.

Ўаблоны располагать тут:
%QSYSTEM_HOME%/plugins/jrt_template/ticket.jasper - это шаблон талона посетител€
%QSYSTEM_HOME%/plugins/jrt_template/ticket_adv.jasper - это шаблон талона предварительной регистрации

ѕлагин, дл€ передачи каких то значений, использует пол€ (Fields), значение из которых получаютс€ в виде $F{им€_пол€}, например $F{service_name} вернет название услуги, на которую записалс€ пользователь и т.п. —писок всех полей и их значени€ приведены ниже.

ѕараметры дл€ использовани€:
promo_text
bottom_text

ѕараметры простого посетител€:
customer_number
customer_prefix
customer_priority
customer_input_data
customer_id
service_description
service_input_caption
service_name
service_pre_info_text
service_ticket_text
service (это класс QService)
customer (это класс QCustomer)

ѕараметры предварительно зарегистрированного посетител€:
adv_customer_input_data
adv_customer_id
adv_customer
adv_customer_time
service_description
service_input_caption
service_name
service_pre_info_text
service_ticket_text
service (это класс QService)