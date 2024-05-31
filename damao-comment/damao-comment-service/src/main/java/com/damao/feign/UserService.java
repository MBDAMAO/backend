package com.damao.feign;

import com.damao.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name="user-service"
        // url="http://localhost:8080/feign/server/"
        //configuration=FeignInterceptor.class,
        //fallback=TestService.DefaultFallback.class
)
public interface UserService {
    @GetMapping("/batchSelect")
    List<?> query(@RequestBody List<Long> ids);
}
