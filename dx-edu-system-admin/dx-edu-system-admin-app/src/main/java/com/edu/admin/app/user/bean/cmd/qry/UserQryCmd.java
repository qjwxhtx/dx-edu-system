package com.edu.admin.app.user.bean.cmd.qry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQryCmd implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String password;
}
