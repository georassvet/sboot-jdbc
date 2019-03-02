package ru.fishbalka.sbootjdbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.WebContext;
import ru.fishbalka.sbootjdbc.dao.BankAccountDAO;
import ru.fishbalka.sbootjdbc.exception.BankTransactionException;
import ru.fishbalka.sbootjdbc.form.SendMoneyForm;
import ru.fishbalka.sbootjdbc.model.BankAccountInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
public class BankController {

    @Autowired
    private BankAccountDAO bankAccountDAO;

    @RequestMapping(name = "/", method = RequestMethod.GET)
    public String accountList(Model model){
     //   BankAccountDAO bankAccountDAO = new BankAccountDAO();
        List<BankAccountInfo> accountList = bankAccountDAO.getAll();
        model.addAttribute("accountList", accountList);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar calendar = Calendar.getInstance();
        model.addAttribute("dateTime",  dateFormat.format(calendar.getTime()));

      //  WebContext webContext = new WebContext()
        return "accountList";
    }
    @RequestMapping(value = "/sendMoney",method = RequestMethod.GET)
    public String viewSendMoney(Model model){
        SendMoneyForm form = new SendMoneyForm(1L, 2L, 700d);

        model.addAttribute("sendMoneyForm", form);
        return "sendMoneyPage";
    }

    @RequestMapping(value = "/sendMoney", method = RequestMethod.POST)
    public String processSendMoney(Model model, SendMoneyForm sendMoneyForm){
        try{
            bankAccountDAO.sendMoney(sendMoneyForm.getFromAccountId(),
                    sendMoneyForm.getToAccountId(),
                    sendMoneyForm.getAmount());
        }catch (BankTransactionException e){
            model.addAttribute("error",e.getMessage());
            return "/sendMoneyPage";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/createAccount", method = RequestMethod.GET)
    public String createAccount(Model model){
        BankAccountInfo form = new BankAccountInfo();
        model.addAttribute("bankAccountInfo", form);
        return "createAccount";
    }

    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String createAccount(Model model, BankAccountInfo bankAccountInfo){
        bankAccountDAO.create(bankAccountInfo);
        return "redirect:/";
    }

}
