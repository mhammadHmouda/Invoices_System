import {
    DELAY,
    primaryColor,
    dangerColor,
    INVOICES_URL,
  } from "../../settings/settings.js";
  
  const token = localStorage.getItem("token");
  
  if (!token) {
    window.location.href = "login.html";
  }
  
  export function getItem(id) {
  
    return $.ajax({
      url: INVOICES_URL + `/${id}`,
      method: "GET",
      dataType: "json",
      headers: {Authorization: "Bearer " + localStorage.getItem("token")},
      success: function (data) {
      },
    });
  }
  
  export function deleteItem(id) {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: primaryColor,
      cancelButtonColor: dangerColor,
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.isConfirmed) {
        $.ajax({
          url: `${INVOICES_URL}/${id}`,
          type: "DELETE",
          headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
          },
          success: (data) => {
            Swal.fire("Deleted!", data, "success");
            setTimeout(() => {
              location.reload();
            }, DELAY - 1500);
          },
          error: (jqXHR, textStatus, errorThrown) => {
            console.error('Failed to update invoice', errorThrown);
          }
        });
      }
    });
  }
  
  export async function editItem(id) {
    const invoice = await getItem(id)
    const { value: data } = await Swal.fire({
      title: "Edit Price",
      html: `<input id="price" class="swal2-input w-75" value=${invoice.totalPrice} placeholder="Enter total price">`,
      icon: "info",
      focusConfirm: false,
      showCancelButton: true,
      cancelButtonText: "Cancel",
      confirmButtonText: "Edit",
      confirmButtonColor: primaryColor,
      cancelButtonColor: dangerColor,
      preConfirm: () => {
        const price = invoice.totalPrice;
        invoice.totalPrice = $("#price").val();
        return {
          invoice: invoice,
          totalPrice: price
        }
      },
    });
  
    if (!data) return;
  
    if (data.invoice.totalPrice != data.totalPrice) {
        console.log(data)

        $.ajax({
            url: INVOICES_URL,
            type: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + localStorage.getItem('token'),
            },
            data: JSON.stringify(data.invoice),
            success: (responseText) => {
                Swal.fire("Edited!", responseText, "success");
                setTimeout(() => {
                location.reload();
                }, DELAY - 1500);
            },
            error: (jqXHR, textStatus, errorThrown) => {
              console.error('Failed to update invoice', errorThrown);
            }
          });
    }
    else{
        return Swal.fire("Error!", "Please change the total price", "error");
    }
   
  }
  
  
  
  
  export function goToNewPageWithID(item) {
    function generateItemsHTML(items) {
      return items
        .map((item) => {
          return `
            <tr>
              <td>${item.id}</td>
              <td>${item.item.name}</td>
              <td>${item.quantity}</td>
              <td>${item.item.unitPrice}</td>
            </tr>
          `;
        })
        .join("");
    }
  
    const html = `
      <!DOCTYPE html>
      <html>
        <head>
          <title>Item Details</title>
          <style>
            body {
              margin: 0;
            }
  
            .item-details {
              font-family: Arial, sans-serif;
              max-width: 600px;
              margin: 0 auto;
              padding: 20px;
            }
  
            h2 {
              margin-top: 20px;
            }
  
            .details-container {
              margin-bottom: 20px;
            }
  
            .details-label {
              font-weight: bold;
              display: inline-block;
              width: 100px;
            }
  
            table {
              border-collapse: collapse;
              width: 100%;
            }
  
            th,
            td {
              padding: 8px;
              text-align: left;
              border-bottom: 1px solid #ddd;
            }
  
            th {
              background-color: #f2f2f2;
            }
  
            .scrollable-table {
              max-height: 750px;
              overflow-y: auto;
            }
  
            .back-button {
              background-color:black;
              color: white;
              border-radius:5px;
              position: fixed;
              top: 30px;
              left: 60px;
            }
          </style>
        </head>
        <body>
          <div class="item-details">
            <h2>Invoice Details</h2>
            <table>
              <thead id="header1">
                <tr>
                  <th>ID</th>
                  <th>Created At</th>
                  <th>Total Price</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>${item.id}</td>
                  <td>${item.createdAt}</td>
                  <td>${item.totalPrice}</td>
                </tr>
              </tbody>
            </table>
  
            <h2>Items</h2>
  
            <div class="scrollable-table">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Quantity</th>
                    <th>Unit Price</th>
                  </tr>
                </thead>
                <tbody>
                  ${generateItemsHTML(item.invoiceItems)}
                </tbody>
              </table>
            </div>
          </div>
          <button class="back-button" onclick="window.location.href = '../../pages/pagintaed.html'">Back</button>
        </body>
      </html>
    `;
  
    document.documentElement.innerHTML = html;
  }