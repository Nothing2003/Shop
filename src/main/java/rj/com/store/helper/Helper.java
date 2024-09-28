package rj.com.store.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {


    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){
        List<U> entity=page.getContent();
        List<V> dtoList= entity
                .stream()
                .map(object -> new ModelMapper().map(object,type))
                .collect(Collectors.toList());
        return new PageableResponse<>(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
