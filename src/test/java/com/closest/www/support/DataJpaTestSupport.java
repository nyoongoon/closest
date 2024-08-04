package com.closest.www.support;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@Import(DataJpaTestConfiguration.class)
@DataJpaTest
public class DataJpaTestSupport {

}
