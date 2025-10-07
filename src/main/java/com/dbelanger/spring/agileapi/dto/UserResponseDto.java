package com.dbelanger.spring.agileapi.dto;
public class UserResponseDto { public long id; public String name; public String email; public long organizationId; public UserResponseDto(long id,String name,String email,long organizationId){this.id=id;this.name=name;this.email=email;this.organizationId=organizationId;} }
