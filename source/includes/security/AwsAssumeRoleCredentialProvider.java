public class AwsAssumeRoleCredentialProvider implements CustomCredentialProvider {

  public AwsAssumeRoleCredentialProvider() {}
  @Override
  public MongoCredential getCustomCredential(Map<?, ?> map) {
    AWSCredentialsProvider provider = new DefaultAWSCredentialsProviderChain();
    Supplier<AwsCredential> awsFreshCredentialSupplier = () -> {
      AWSSecurityTokenService stsClient = AWSSecurityTokenServiceAsyncClientBuilder.standard()
          .withCredentials(provider)
          .withRegion("us-east-1")
          .build();
      AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest().withDurationSeconds(3600)
          .withRoleArn((String)map.get("mongodbaws.auth.mechanism.roleArn"))
          .withRoleSessionName("Test_Session");
      AssumeRoleResult assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);
      Credentials creds = assumeRoleResult.getCredentials();
      // Add your code to fetch new credentials
      return new AwsCredential(creds.getAccessKeyId(), creds.getSecretAccessKey(), creds.getSessionToken());
    };
    return MongoCredential.createAwsCredential(null, null)
        .withMechanismProperty(MongoCredential.AWS_CREDENTIAL_PROVIDER_KEY, awsFreshCredentialSupplier);
  }

  // Validates presence of an ARN
  @Override
  public void validate(Map<?, ?> map) {
    String roleArn = (String) map.get("mongodbaws.auth.mechanism.roleArn");
    if (StringUtils.isNullOrEmpty(roleArn)) {
      throw new RuntimeException("Invalid value set for customProperty");
    }
  }

  // Initializes the custom provider
  @Override
  public void init(Map<?, ?> map) {

  }
}