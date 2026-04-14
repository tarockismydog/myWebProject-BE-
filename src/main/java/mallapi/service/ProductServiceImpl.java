package mallapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mallapi.domain.Product;
import mallapi.domain.ProductImage;
import mallapi.dto.PageRequestDTO;
import mallapi.dto.PageResponseDTO;
import mallapi.dto.ProductDTO;
import mallapi.repository.ProductRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize(), Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);
        // Object[] => 0 product 1 productImage
        // Object[] => 0 product 1 productImage
        // Object[] => 0 product 1 productImage

        List<ProductDTO> dtoList = result.get().map(arr -> {
            ProductDTO productDTO = null;
            
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();
            
            String imageStr = productImage.getFileName();
            productDTO.setUploadFileNames(List.of(imageStr));

            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long register(ProductDTO productDTO) {

        Product product = dtoToEntity(productDTO); // dto로 entity로 바꾸는 과정.. 왜? (등록할때 매번 해줘야하는건가..)

        log.info("-----------------------------");
        log.info(product);
        log.info(product.getImageList());

        Long pno = productRepository.save(product).getPno();

        return pno;
    }

    @Override
    public ProductDTO get(Long pno) {

        // 여기에서도 entity를 dto로 변환하는 과정이 왜 필요한거지?
        Optional<Product> result = productRepository.findById(pno); // 왜 Optional로 받지? JPA에서 정해놓은거라 그런거겟지
        
        Product product = result.orElseThrow();

        ProductDTO productDTO = entityToDTO(product);

        return productDTO;
    }

    @Override
    public void modify(ProductDTO productDTO) {
        // 조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();

        // 변경 내용 반영
        product.changePrice(productDTO.getPrice());
        product.changePname(productDTO.getPname());
        product.changePdesc(productDTO.getPdesc());
        product.changeDel(productDTO.isDelFlag());

        // 이미지 처리
        List<String> uploadFileName = productDTO.getUploadFileNames();

        // 기존에 product가 가지고 있던 "이미지 리스트(DB 관계)"를 비움
        // ❗ 실제 서버 파일은 삭제되지 않음 (파일은 그대로 존재)
        // ❗ DB도 아직 안 지워짐 (단지 메모리 상태만 바뀜)
        product.clearList();

        // 프론트에서 넘어온 "최종 이미지 목록"
        // (기존 유지할 파일 + 새로 업로드되어 UUID 붙은 파일)
        if(uploadFileName != null) {
            uploadFileName.forEach(uploadName -> {
                // 현재 product 객체에 이미지 다시 추가
                // 👉 이 시점에서 메모리 상태는 "최종 상태"가 됨
                // 예: [b.jpg, d.jpg]
                product.addImageString(uploadName);
            });
        }

        // 🔥 여기서 진짜 DB 반영 발생, DB에는 파일 "이름"만 저장됨
        productRepository.save(product);

        /*
        * ✔ 핵심 동작 (JPA가 내부적으로 처리)
        *
        * 1. 기존 DB 상태와 현재 객체 상태를 비교함
        *    기존: [a, b, c]
        *    현재: [b, d]
        *
        * 2. 변경 감지 (Dirty Checking)
        *
        * 3. orphanRemoval = true라면:
        *    - a, c → DELETE 쿼리 실행
        *
        * 4. 새로 추가된 d → INSERT 쿼리 실행
        *
        * 👉 결과적으로 DB에는 [b, d]만 남음
        *
        * ❗ 우리는 DELETE/INSERT 쿼리를 직접 안 썼지만
        *    JPA가 자동으로 생성해서 실행함
        */
    }

    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);
    }

    private ProductDTO entityToDTO(Product product) {

        ProductDTO productDTO = ProductDTO.builder()
                                    .pno(product.getPno())
                                    .pname(product.getPname())
                                    .pdesc(product.getPdesc())
                                    .price(product.getPrice())
                                    .delFlag(product.isDelFlag())
                                    .build();

        List<ProductImage> imageList = product.getImageList();
        
        if(imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(productImage -> 
            productImage.getFileName()).toList();

        productDTO.setUploadFileNames(fileNameList);

        return productDTO;
    }


    private Product dtoToEntity(ProductDTO productDTO) {
        
        Product product = Product.builder()
                            .pno(productDTO.getPno())
                            .pname(productDTO.getPname())
                            .pdesc(productDTO.getPdesc())
                            .price(productDTO.getPrice())
                            .build();

        List<String> uploadFileName = productDTO.getUploadFileNames();

        if(uploadFileName == null || uploadFileName.size() == 0) {
            return product;
        }

        uploadFileName.forEach(fileName -> {
            product.addImageString(fileName);
        });

        return product;
    }


}
