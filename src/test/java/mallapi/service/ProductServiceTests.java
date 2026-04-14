package mallapi.service;

import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;
import mallapi.dto.PageRequestDTO;
import mallapi.dto.PageResponseDTO;
import mallapi.dto.ProductDTO;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Disabled
    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);

        log.info(responseDTO.getDtoList());
    }

    @Disabled
    @Test
    public void testRegister() {

         ProductDTO productDTO = ProductDTO.builder()
                                    .pname("새로운 상품")
                                    .pdesc("추가상품입니다.")
                                    .price(1000)
                                    .build();

        productDTO.setUploadFileNames(
            java.util.List.of(
                UUID.randomUUID()+"_"+"Test1.jpg",
                UUID.randomUUID()+"_"+"Test2.jpg"
            )
        );

        productService.register(productDTO);
    }

}
