package mallapi.service;

import org.springframework.transaction.annotation.Transactional;

import mallapi.dto.PageRequestDTO;
import mallapi.dto.PageResponseDTO;
import mallapi.dto.ProductDTO;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    void modify(ProductDTO productDTO);

    void remove(Long pno);
}
