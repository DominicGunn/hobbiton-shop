package com.hobbiton.shop.web.packages;

import com.hobbiton.shop.persistence.packages.PackageService;
import com.hobbiton.shop.persistence.packages.models.ShopPackage;
import com.hobbiton.shop.web.packages.models.PackageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * @author Dominic Gunn
 */
@RestController
public class PackageController {

    private static final String DEFAULT_CURRENCY_CODE = "USD";

    @Autowired
    private PackageService packageService;

    @RequestMapping(value = "/packages", method = RequestMethod.GET)
    public ResponseEntity<List<ShopPackage>> getPackages(
            @RequestParam(value = "currencyCode", required = false, defaultValue = DEFAULT_CURRENCY_CODE) String currencyCode) {
        return ResponseEntity.ok(packageService.getPackages(currencyCode));
    }

    @RequestMapping(value = "/packages/{packageId}", method = RequestMethod.GET)
    public ResponseEntity<ShopPackage> getPackage(
            @PathVariable("packageId") Long packageId,
            @RequestParam(value = "currencyCode", required = false, defaultValue = DEFAULT_CURRENCY_CODE) String currencyCode) {
        return ResponseEntity.ok(packageService.getPackage(packageId, currencyCode));
    }

    @RequestMapping(value = "/packages", method = RequestMethod.POST)
    public ResponseEntity<ShopPackage> savePackage(
            @RequestBody PackageRequest packageRequest,
            @RequestParam(value = "currencyCode", required = false, defaultValue = DEFAULT_CURRENCY_CODE) String currencyCode) {
        final ShopPackage shopPackage = packageService.savePackage(
                packageRequest.getName(), packageRequest.getDescription(), packageRequest.getProductIds(), currencyCode
        );
        final URI resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(shopPackage.getId()).toUri();
        return ResponseEntity.created(resourceLocation).body(shopPackage);
    }

    @RequestMapping(value = "/packages/{packageId}", method = RequestMethod.PUT)
    public ResponseEntity<ShopPackage> updatePackage(
            @PathVariable("packageId") Long packageId, @RequestBody PackageRequest packageRequest,
            @RequestParam(value = "currencyCode", required = false, defaultValue = DEFAULT_CURRENCY_CODE) String currencyCode) {
        final ShopPackage shopPackage = packageService.updatePackage(
                packageId, packageRequest.getName(), packageRequest.getDescription(), packageRequest.getProductIds(), currencyCode
        );
        return ResponseEntity.ok(shopPackage);
    }

    @RequestMapping(value = "/packages/{packageId}", method = RequestMethod.DELETE)
    public ResponseEntity<ShopPackage> getPackage(@PathVariable("packageId") Long packageId) {
        packageService.deletePackage(packageId);
        return ResponseEntity.ok().build();
    }
}
