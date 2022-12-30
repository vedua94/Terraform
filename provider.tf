// Configure the Google Cloud provider
provider "google" {
  credentials = file("/cred.json")
  project     = var.project
  region      = var.region
}
