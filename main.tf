# Copies the file as the root user using SSH
provisioner "file" {
  source      = "LAB1-count-messages/app.js"
  destination = "/home/azureuser/app.js"

  connection {
    type     = "ssh"
    user     = "azureuser"
    password = "${var.root_password}"
    host     = "${var.host}"
  }
}