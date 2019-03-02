package ru.fishbalka.sbootjdbc.mapper;

import ru.fishbalka.sbootjdbc.model.BankAccountInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccountMapper {
    public static BankAccountInfo mapModel(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String fullName = resultSet.getString("full_name");
        Double balance = resultSet.getDouble("balance");
        return new BankAccountInfo(id,fullName,balance);
    }
}
