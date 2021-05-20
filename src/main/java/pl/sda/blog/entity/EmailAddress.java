package pl.sda.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EmailAddress {
    @Column(nullable = false)
    private String email;
    public String username(){
        return email.substring(0, email.indexOf('@'));
    }
}
