package com.edu.netc.bakensweets.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDTO {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String gender;
    private String imgUrl;
}
