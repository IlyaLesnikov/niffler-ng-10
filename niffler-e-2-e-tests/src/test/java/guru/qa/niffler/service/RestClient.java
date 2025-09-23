package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

abstract class RestClient {
  private final Config CFG = Config.getInstance();
  protected final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.frontUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();
}
