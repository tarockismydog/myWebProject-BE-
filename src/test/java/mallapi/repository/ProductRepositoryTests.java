package mallapi.repository;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import mallapi.domain.Product;
import mallapi.dto.PageRequestDTO;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired 
    private ProductRepository productRepository;

    @Disabled
    @Test
    public void testInsert() {

        for(int i = 0; i < 20 ; i++) {
            Product product = Product.builder()
                                    .pname("Test Product" + i)
                                    .price(10000)
                                    .pdesc("Test Product Description")
                                    .build();
    
            product.addImageString(UUID.randomUUID()+"_"+"IMAGE1.jpg");
            product.addImageString(UUID.randomUUID()+"_"+"IMAGE2.jpg");
            
            productRepository.save(product);
        }
    }

    @Disabled
    @Test
    public void testRead() {
        Long pno =12L;
        Optional<Product> result = productRepository.selectOne(pno);
        Product product = result.orElseThrow();
        log.info(product);
        log.info(product.getImageList());
    }

    @Disabled
    @Commit
    @Transactional
    @Test
    public void testDelete() {
        Long pno = 1L;
        productRepository.updateToDelete(pno, true);
    }

    @Disabled
    @Test
    public void testUpdate() {
        Product product = productRepository.selectOne(1L).get();
        
        product.changePrice(2000);
        product.clearList();
        
        product.addImageString(UUID.randomUUID()+"_"+"PIMAGE1.jpg");
        product.addImageString(UUID.randomUUID()+"_"+"PIMAGE2.jpg");
        product.addImageString(UUID.randomUUID()+"_"+"PIMAGE3.jpg");

        productRepository.save(product);
    }

    @Disabled
    @Test
    public void testList() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);
        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)) );
    }

    @Disabled
    @Test
    public void testSearch() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        productRepository.searchList(pageRequestDTO);
    }


}
