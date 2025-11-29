package com.example.MocBE.mapper;


import com.example.MocBE.dto.response.CustomerResponse;
import com.example.MocBE.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse toDto(Customer customer);
    List<CustomerResponse> toDtoList(List<Customer> customers);

}
