provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "main" {
  name     = "int493"
  location = "southeastasia"
}

resource "azurerm_virtual_network" "main" {
  name                = "int493-vnet"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
}

resource "azurerm_subnet" "internal" {
  name                 = "internal"
  resource_group_name  = azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes     = ["10.0.2.0/24"]
}

resource "azurerm_public_ip" "main" {
  name                = "lab1-ip"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  allocation_method   = "Static"
}

resource "azurerm_network_interface" "main" {
  name                = "lab1597"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.internal.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.main.id
  }
}

resource "azurerm_linux_virtual_machine" "main" {
  name                            = "lab1"
  resource_group_name             = azurerm_resource_group.main.name
  location                        = azurerm_resource_group.main.location
  size                            = "Standard_B1s"
  admin_username                  = "azureuser"
  admin_password                  = "${var.root_password}"
  disable_password_authentication = false
  network_interface_ids = [
    azurerm_network_interface.main.id,
  ]

  source_image_reference {
    publisher = "Canonical"
    offer     = "UbuntuServer"
    sku       = "20.04-LTS"
    version   = "latest"
  }

  os_disk {
    storage_account_type = "Standard_LRS"
    caching              = "ReadWrite"
  }

  provisioner "remote-exec" {
    inline = [
        "sudo apt-get update",
        "sudo apt install npm -y",
        "sudo apt install npm -y",
        "sudo npm install pm2 -g",
        "cd /home/azureuser/teraform",
        "git clone https://github.com/miechayakorn/INT493-SoftwareArchitec-Lab.git",
        "cd LAB1-count-messages",
        "npm install",
        "pm2 start npm -- start",
      ]

    connection {
      type     = "ssh"
      user     = "azureuser"
      password = "${var.root_password}"
      host     = "self.public_ip_address"
    }
  } 
}
