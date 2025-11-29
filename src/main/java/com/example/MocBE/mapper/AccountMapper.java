package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.AccountResponse;
import com.example.MocBE.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    AccountResponse toDto(Account account);

    List<AccountResponse> toDtoList(List<Account> accounts);
}
