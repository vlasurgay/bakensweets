package com.edu.netc.bakensweets.repository;

import com.edu.netc.bakensweets.model.Account;
import com.edu.netc.bakensweets.repository.interfaces.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepositoryImpl extends BaseJdbsRepository implements AccountRepository {

    @Value("${sql.account.create}")
    private String sqlQueryCreate;
    @Value("${sql.account.findById}")
    private String sqlQueryGetById;
    @Value("${sql.account.findByEmail}")
    private String sqlQueryFindByEmail;

    public AccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void create(Account account) {
        jdbcTemplate.update(sqlQueryCreate, account.getId(), account.getFirstName(), account.getLastName(),
                account.getBirthDate(), account.getGender().name(), account.getAccountRole().getAuthority());
    }

    @Override
    public void update(Account account) {
        //jdbcTemplate.update(sqlQueryCreate, account.getId(), account.getFirstName(), account.getLastName(),
                //account.getBirthDate(), account.getGender(), account.getImgUrl(), account.getAccountRole());
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Account findById(Long id) {
        return jdbcTemplate.queryForObject(sqlQueryGetById, new BeanPropertyRowMapper<>(Account.class), id);
    }

    @Override
    public Account findByEmail(String email) {
        return jdbcTemplate.queryForObject(sqlQueryFindByEmail, new BeanPropertyRowMapper<>(Account.class), email);
    }
}
