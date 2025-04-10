package kr.hhplus.be.server.interfaces.api.common;

import java.util.List;

// record 의 모든 필드는 불변(final) 객체다
public record ResponseItem<T>(
    String type,
    T data
) {
    // static(객체화) method 에서는 generic 을 사용할 수 없다
    // static 은 메모리에 미리 올라가는데, type 인 T 가 정해지지 않았기 때문!
    //    public static Map<?, ?> of(String type, T dto) {
    //        return new ResponseData<T>(dto, type);
    //    }
    
    // 아래처럼 static method 안에서 generic type 을 선언하면, method 전용 타입 파라미터로 생성 되어 문제 없음
    public static <T> ResponseItem<T> of(T data) throws RuntimeException{
        String type;
        if (data instanceof List){
            type = "list";
        }else if (data instanceof java.lang.Record) {
            type = "object";
        }else{
            throw new RuntimeException("list or record required");
        }
        return new ResponseItem<T>(type, data);
    }
}
