package com.hobbiton.shop.persistence.packages;

import javax.persistence.*;
import java.util.List;

/**
 * @author Dominic Gunn
 */
@Entity
@Table(name="packages")
public class PackageDto {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name="packages_products")
    private List<String> productIds;

    public PackageDto() {
        // Default constructor.
    }

    public PackageDto(String name, String description, List<String> productIds) {
        this.name = name;
        this.description = description;
        this.productIds = productIds;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getProductIds() {
        return productIds;
    }
}
