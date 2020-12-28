package com.hompan.rpc.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Hom Pan
 * @date 2020/12/11 13:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Info implements Serializable {
    private Integer id;
    private String name;
    private String msg;
}
