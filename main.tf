# Copies the file as the root user using SSH
provisioner "file" {
  source      = "LAB1-count-messages/"
  destination = "/etc/myapp.conf"

  connection {
    type     = "ssh"
    user     = "azureuser"
    password = "${var.root_password}"
    host     = "${var.host}"
  }
}