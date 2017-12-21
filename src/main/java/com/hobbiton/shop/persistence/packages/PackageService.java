package com.hobbiton.shop.persistence.packages;

import com.hobbiton.shop.fixer.FixerService;
import com.hobbiton.shop.persistence.packages.models.ShopPackage;
import com.hobbiton.shop.products.ProductService;
import com.hobbiton.shop.products.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Dominic Gunn
 */
@Service
@Transactional
public class PackageService {

    @Autowired
    private FixerService fixerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PackageRepository packageRepository;

    public ShopPackage savePackage(String name, String description, List<String> productId) {
        // TODO: Verify products exist, move currency conversion to productService, create PackageControllerAdvice.
        final PackageDto packageDto = packageRepository.save(new PackageDto(name, description, productId));
        return preparePackage(packageDto, "USD");
    }

    public ShopPackage getPackage(Long packageId, String currency) {
        return preparePackage(packageRepository.findOne(packageId), currency);
    }

    public List<ShopPackage> getPackages(String currency) {
        return StreamSupport.stream(packageRepository.findAll().spliterator(), false)
                .map(packageDto -> preparePackage(packageDto, currency))
                .collect(Collectors.toList());
    }

    /**
     * This method prepares a ShopPackage in a currency requested by a client.
     * @param packageDto The package stored in the database.
     * @param currency The currency requested by the client (USD by default).
     * @return ShopPackage containing product information and currency requested by client.
     */
    private ShopPackage preparePackage(PackageDto packageDto, String currency) {
        final List<Product> packageProductList = new ArrayList<>();
        for (String productId : packageDto.getProductIds()) {
            final Product product = productService.getProduct(productId);
            product.setPrice(convertPrice(currency, product.getUsdPrice()));
            packageProductList.add(product);
        }

        return new ShopPackage(packageDto.getId(), packageDto.getName(), packageDto.getDescription(), packageProductList);
    }

    private double convertPrice(String currency, double currentPrice) {
        if ("USD".equals(currency)) {
            return currentPrice;
        }
        return fixerService.exchange(currency, currentPrice);
    }
}
