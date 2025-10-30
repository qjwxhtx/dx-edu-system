package com.edu.admin.infra.bean.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 人员mongo实体
 *
 * @author hjw
 * @since 2025-10-24
 */
@Data
@Builder
@Document("User")
@NoArgsConstructor
@AllArgsConstructor
public class UserDocument {

    @Id
    private String id;
    private String name;
    private Integer age;
    private String email;

}
