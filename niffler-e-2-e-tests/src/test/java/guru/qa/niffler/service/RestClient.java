package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

abstract class RestClient {
  private final Config CFG = Config.getInstance();
  private final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
      .setLevel(HttpLoggingInterceptor.Level.BODY);
  private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
      .addInterceptor(loggingInterceptor)
      .build();
  protected final Retrofit retrofit = new Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(CFG.spendApiUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();
}
