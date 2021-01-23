# Copies the file as the root user using SSH
provisioner "remote-exec" {

  connection {
    type     = "ssh"
    user     = "azureuser"
    password = "${var.root_password}"
    host     = "${var.host}"
  }
  inline = [
      "ls",
    ]
}
