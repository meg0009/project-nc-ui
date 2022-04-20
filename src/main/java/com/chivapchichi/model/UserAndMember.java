package com.chivapchichi.model;

import lombok.Data;

import javax.validation.Valid;

@Data
public class UserAndMember {

    @Valid
    private Users users;

    @Valid
    private Members members;
}
