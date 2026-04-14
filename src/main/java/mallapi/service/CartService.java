package mallapi.service;

import java.util.List;

import jakarta.transaction.Transactional;
import mallapi.dto.CartItemDTO;
import mallapi.dto.CartItemListDTO;

@Transactional
public interface CartService {
    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    List<CartItemListDTO> getCartItems(String email);

    List<CartItemListDTO> remove(Long cino);

}
