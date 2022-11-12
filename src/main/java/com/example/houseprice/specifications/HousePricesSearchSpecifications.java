package com.example.houseprice.specifications;

import com.example.houseprice.models.HousePrices;
import com.example.houseprice.utils.Utils;
import org.springframework.data.jpa.domain.Specification;

public class HousePricesSearchSpecifications {
    public static Specification<HousePrices> withId(Long id){
        return (root, query, cb) -> id != null ? cb.equal(root.get("id"), id) : cb.conjunction();
    }
    public static Specification<HousePrices> withAccommodates(Integer accommodates){
        return (root, query, cb) -> accommodates != null ? cb.equal(root.get("accommodates"), accommodates) : cb.conjunction();
    }
    public static Specification<HousePrices> withBedType(String bedType){
        return (root, query, cb) -> !Utils.isNullOrEmpty(bedType) ?
                cb.equal(root.join("bedType").get("type"), bedType) : cb.conjunction();
    }
    public static Specification<HousePrices> withBathrooms(Double bathrooms){
        return (root, query, cb) -> bathrooms != null ? cb.equal(root.get("bathrooms"), bathrooms) : cb.conjunction();
    }
    public static Specification<HousePrices> withBedRooms(Integer bedrooms){
        return (root, query, cb) -> bedrooms != null ? cb.equal(root.get("bedrooms"), bedrooms) : cb.conjunction();
    }
}
