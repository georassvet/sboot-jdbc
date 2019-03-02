package ru.fishbalka.sbootjdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fishbalka.sbootjdbc.exception.BankTransactionException;
import ru.fishbalka.sbootjdbc.mapper.BankAccountMapper;
import ru.fishbalka.sbootjdbc.model.BankAccountInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class BankAccountDAO extends AbstractDAO<BankAccountInfo, Long> {

    @Autowired
    public BankAccountDAO(){

    }

    String query = "SELECT id, full_name, balance FROM bank_account";

    @Override
    public List<BankAccountInfo> getAll() {
       List<BankAccountInfo> list = new ArrayList<>();
        Statement statement = this.getStatement();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
              BankAccountInfo bankAccountInfo = BankAccountMapper.mapModel(resultSet);
              list.add(bankAccountInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            this.closeStatement(statement);
        }
        return list;
    }

    @Override
    public BankAccountInfo findById(Long id) {
        Statement statement = this.getStatement();
        BankAccountInfo bankAccountInfo =null;
        try {
            ResultSet resultSet =  statement.executeQuery(query + " WHERE id =" + id);
            if(resultSet.next()){
                bankAccountInfo = BankAccountMapper.mapModel(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            this.closeStatement(statement);
        }
        return bankAccountInfo;
    }

    @Override
    public void update(BankAccountInfo item) {
        Statement statement = this.getStatement();
        try {
            String query = "UPDATE bank_account SET balance="
                    + item.getBalance() + " WHERE id="
                    + item.getId();
            statement.executeUpdate(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public void delete(Long id) {
    }
    @Override
    public void create(BankAccountInfo item){
        Statement statement = this.getStatement();
        String query = "INSERT INTO bank_account(id, full_name, balance) VALUES ("
                + item.getId()+",'"
                +item.getFullName()+"',"
                +item.getBalance()+")";
        try {
            statement.execute(query);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    @Transactional(propagation = Propagation.MANDATORY)
    public void addAmount(Long id, Double amount ) throws BankTransactionException{
        BankAccountInfo bankAccountInfo = this.findById(id);
        if(bankAccountInfo==null){
            throw new BankTransactionException("Account not found "+id);
        }
        double newBalance = bankAccountInfo.getBalance() + amount;
        if(newBalance < 0){
            throw new BankTransactionException("not enough money in the account");
        }
        bankAccountInfo.setBalance(newBalance);
        this.update(bankAccountInfo);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BankTransactionException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, Double amount)throws BankTransactionException{
        addAmount(toAccountId,amount);
        addAmount(fromAccountId,-amount);
    }
}


