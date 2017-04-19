package jace.shim.springcamp2017.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig extends RedisHttpSessionConfiguration {

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}
//
//	@Bean
//	public RedisTemplate<Object, Object> sessionRedisTemplate(
//		RedisConnectionFactory connectionFactory) {
//		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
//		template.setKeySerializer(new StringRedisSerializer());
//		//template.setHashKeySerializer(new StringRedisSerializer());
//		template.setHashValueSerializer(new StringRedisSerializer());
//		template.setConnectionFactory(connectionFactory);
//		return template;
//	}

}
