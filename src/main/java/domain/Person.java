package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:30
 */
@Getter
@Setter
@NoArgsConstructor
public class Person implements Serializable {
    private static final long serialVersionUID = 3903978687528338665L;

    private Long id;

    private String name;
}
