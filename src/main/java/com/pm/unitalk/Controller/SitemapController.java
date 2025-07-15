package com.pm.unitalk.Controller;

import com.pm.unitalk.DTOs.CategoryDto;
import com.pm.unitalk.DTOs.ProductDTO;
import com.pm.unitalk.Model.Category;
import com.pm.unitalk.Service.CategoryService;
import com.pm.unitalk.Service.ProductService;
import com.pm.unitalk.Utility.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller for generating a dynamic sitemap.xml
 * This is an alternative to the static sitemap.xml file in the resources directory.
 * In a production environment, this would be the preferred approach to ensure
 * the sitemap always contains up-to-date URLs.
 */
@Controller
public class SitemapController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/dynamic-sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String generateSitemap() {
        StringBuilder sitemap = new StringBuilder();
        sitemap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Add home page
        sitemap.append("  <url>\n");
        sitemap.append("    <loc>https://halalhaven.kr/</loc>\n");
        sitemap.append("    <changefreq>daily</changefreq>\n");
        sitemap.append("    <priority>1.0</priority>\n");
        sitemap.append("  </url>\n");

        // Add authentication pages
        sitemap.append("  <url>\n");
        sitemap.append("    <loc>https://halalhaven.kr/login</loc>\n");
        sitemap.append("    <changefreq>monthly</changefreq>\n");
        sitemap.append("    <priority>0.5</priority>\n");
        sitemap.append("  </url>\n");
        sitemap.append("  <url>\n");
        sitemap.append("    <loc>https://halalhaven.kr/register</loc>\n");
        sitemap.append("    <changefreq>monthly</changefreq>\n");
        sitemap.append("    <priority>0.5</priority>\n");
        sitemap.append("  </url>\n");

        // Add search page
        sitemap.append("  <url>\n");
        sitemap.append("    <loc>https://halalhaven.kr/search</loc>\n");
        sitemap.append("    <changefreq>weekly</changefreq>\n");
        sitemap.append("    <priority>0.7</priority>\n");
        sitemap.append("  </url>\n");

        // Add all product pages
        PostResponse productResponse = productService.getAllPost(0, 1000, "id", "asc");
        List<ProductDTO> products = productResponse.getContent();
        for (ProductDTO product : products) {
            sitemap.append("  <url>\n");
            sitemap.append("    <loc>https://halalhaven.kr/product/").append(product.getId()).append("</loc>\n");
            sitemap.append("    <changefreq>weekly</changefreq>\n");
            sitemap.append("    <priority>0.8</priority>\n");
            sitemap.append("  </url>\n");
        }

        // Add all category pages
        List<CategoryDto> categories = categoryService.getCategories(0, 1000, "categoryId", "asc");
        for (CategoryDto category : categories) {
            sitemap.append("  <url>\n");
            sitemap.append("    <loc>https://halalhaven.kr/category/").append(category.getCategoryId()).append("</loc>\n");
            sitemap.append("    <changefreq>weekly</changefreq>\n");
            sitemap.append("    <priority>0.8</priority>\n");
            sitemap.append("  </url>\n");
        }

        sitemap.append("</urlset>");
        return sitemap.toString();
    }
}
