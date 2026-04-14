package mallapi.repository.search;

import org.springframework.data.domain.PageRequest;

import mallapi.dto.PageRequestDTO;
import mallapi.dto.PageResponseDTO;
import mallapi.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);
}
