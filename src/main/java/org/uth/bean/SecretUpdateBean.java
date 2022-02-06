package org.uth.bean;

import lombok.Data;

@Data
public class SecretUpdateBean {
    private String namespace;
    private String secretName;
    private String secretKey;
    private String secretValue;
}
