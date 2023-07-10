package com.harri.training1.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoMapper<T1, T2> {

    private final ModelMapper modelMapper;

    public T2 toDto(T1 model, Class<T2> dtoClass){
        return modelMapper.map(model, dtoClass);
    }

    public T1 toModel(T2 dto, Class<T1> modelClass){
        return modelMapper.map(dto, modelClass);
    }
}
