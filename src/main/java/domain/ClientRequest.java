package domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:28
 */
@Getter
@Setter
@NoArgsConstructor
public class ClientRequest implements Serializable {
    private static final long serialVersionUID = -4515553185017953863L;

    private Long id;
}
