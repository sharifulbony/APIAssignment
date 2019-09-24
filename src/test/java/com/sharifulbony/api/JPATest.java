package com.sharifulbony.api;

import com.sharifulbony.api.product.ProductEntity;
import com.sharifulbony.api.product.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JPATest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindByName_thenReturnProduct() {
        // given
        ProductEntity sampleTestProduct = new ProductEntity("sampleTestProduct");
        entityManager.persist(sampleTestProduct);
        entityManager.flush();

//         when
        ProductEntity found = productRepository.findByName(sampleTestProduct.getName());

        // then
        assertThat(found.getName())
                .isEqualTo(sampleTestProduct.getName());
    }

    @Test
    public void checkIfUpdateProduct() {
        // given
        ProductEntity sampleTestProduct = new ProductEntity("sampleTestProduct");
        entityManager.persist(sampleTestProduct);
        entityManager.flush();

//         when
        String changedName="changedName";
        sampleTestProduct.setName(changedName);

        entityManager.persistAndFlush(sampleTestProduct);

        // then
        assertThat(sampleTestProduct.getName())
                .isEqualTo(changedName);
    }
}
