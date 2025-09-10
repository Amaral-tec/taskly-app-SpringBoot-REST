package com.amaral.taskly.model;

import java.util.Date;

import org.hibernate.annotations.Filter;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
@Table(name = "accesses")
@SequenceGenerator(name = "seq_access", sequenceName = "seq_access", initialValue = 1, allocationSize = 1)
public class Access implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_access")
	private Long id;

	@NotBlank
	@Column(nullable = false, unique = true)
	private String name;

    @Temporal(TemporalType.DATE)
	@Column(name = "created_at")
	private Date createdAt;
	
    @Column(nullable = false)
	private Boolean deleted = Boolean.FALSE;

    @Override
    public String getAuthority() {
        return this.name;
    }

}
