import {
    DELAY,
    primaryColor,
    dangerColor,
    ALL_LOGS_URL,
  } from "../../settings/settings.js";
  
  const token = localStorage.getItem("token");
  
  if (!token) {
    window.location.href = "login.html";
  }
  
  export function getLog(id) {
  
    return $.ajax({
      url: ALL_LOGS_URL + `/${id}`,
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
          url: `${ALL_LOGS_URL}/${id}`,
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
            console.error('Failed to delete log', errorThrown);
          }
        });
      }
    });
  }
  
  export async function editItem(id) {
    const log = await getLog(id)
    const { value: data } = await Swal.fire({
      title: "Edit Description",
      html: `<input id="description" class="swal2-input w-115" value="${log.description}" placeholder="Enter description">`,
      icon: "info",
      focusConfirm: false,
      showCancelButton: true,
      cancelButtonText: "Cancel",
      confirmButtonText: "Edit",
      confirmButtonColor: primaryColor,
      cancelButtonColor: dangerColor,
      preConfirm: () => {
        const description = log.description;
        log.description = $("#description").val();
        return {
          log: log,
          description: description
        }
      },
    });

  
    if (!data) return;
  
    if (data.log.description != data.description) {

        $.ajax({
            url: ALL_LOGS_URL,
            type: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + localStorage.getItem('token'),
            },
            data: JSON.stringify(data.log),
            success: (responseText) => {
                Swal.fire("Edited!", responseText, "success");
                setTimeout(() => {
                location.reload();
                }, DELAY - 1500);
            },
            error: (jqXHR, textStatus, errorThrown) => {
              console.error('Failed to update log', errorThrown);
            }
          });
    }
    else{
        return Swal.fire("Error!", "Please change the description", "error");
    }
   
  }
  
  
  
  
  export function goToNewPageWithID(item) {
    function generateItemsHTML(items) {
      return items
        .map((item) => {
          return `
            <tr>
              <td>${item.id}</td>
              <td>${item.name}</td>
              <td>${item.quantity}</td>
              <td>${item.unitPrice}</td>
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
                  ${generateItemsHTML(item.items)}
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