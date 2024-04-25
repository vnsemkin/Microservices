ui = true

storage "postgresql" {
  connection_url = "postgres://postgres:postgres@database:5432/vault"
}


listener "tcp" {
  address     = "0.0.0.0:8200"
  tls_disable = 1
}

disable_mlock = true