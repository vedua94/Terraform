terraform {
  backend "gcs"{
    bucket      = "ankit-terraform-state"
    prefix      = "dev"
    credentials = "cred.json"
  }
}
