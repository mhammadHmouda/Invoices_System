import { parseJwt} from "../../utils/tokenUtils.js"
import { ITEMS_URL, INVOICES_URL, DELAY } from "../../settings/settings.js";
  
  const token = localStorage.getItem('token');

  if(!token){
    location.href = "../../pages/login.html"
  }
  
  const form = document.querySelector("#invoice-form");
  const formContainer = document.querySelector(".form-container");
  const addBtn = document.querySelector("#add-btn");
  const attachmentInput = document.getElementById('attachment');
  const attachmentList = document.getElementById('attachmentList');
  let attachments = [];

  attachmentInput.addEventListener('change', handleAttachments);

  const data = `<div class="row">
                    <div class="d-flex gap-1 justify-content-center align-items-center col-5">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="names[]" class="form-control" />
                    </div>
                    <div class="d-flex gap-1 justify-content-center align-items-center col-6">
                        <label for="quantity">Quantity</label>
                        <input type="text" id="quantity" name="quantities[]" class="form-control" />
                    </div>
                </div>`;
  
  addBtn.addEventListener("click", onBtnClick);
  form.addEventListener("submit", onSubmit);

  function onBtnClick(e) {
    e.preventDefault();
    formContainer.innerHTML += data;
  
    const itemsCount = formContainer.childElementCount;
    if (itemsCount > 3) {
      formContainer.classList.add("scrollable");
    } else {
      formContainer.classList.remove("scrollable");
    }
  }
  
  async function getItemByItemName(name) {
    try {
      const response = await fetch(ITEMS_URL + `/${name}`, {
        method: 'GET',
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('token'),
        },
      });
  
      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Failed to fetch item ID. Response status: ${response.status}. Message: ${errorMessage}`);
      }
      const data = await response.json();
      return data;
    } catch (error) {
      const message = `<div class="alert alert-danger" style = "margin-top:20px" role="alert">This item not exist!</div>`;
      $("#error").append(message);
      setTimeout(() => {
        $("#error").empty();
      }, DELAY);

      throw error;
    }
  }

  async function onSubmit(e) {
    e.preventDefault();  
    const formData = new FormData(form);
    const quantities = formData.getAll("quantities[]");
    const itemNames = formData.getAll("names[]");

    const dataArray = await Promise.all(itemNames.map(async (name, index) => {
      const item = await getItemByItemName(name);
      return {
        item: item,
        quantity: quantities[index],
      };
    }));


    var total = 0;
    for (const item of dataArray) {
      console.log(item);
        const unitPrice = item.item.unitPrice;
        const quantity = parseInt(item.quantity);
        total += (unitPrice * quantity);
    }
  
    const { id } = parseJwt();

    const invoice = {
        totalPrice: total,
        user: { id: id },
        invoiceItems: dataArray
    }
       
    var attachmentData = new FormData();

    attachments.forEach(attachment => attachmentData.append("files", attachment))
      
    attachmentData.append("invoice", JSON.stringify(invoice));

    $.ajax({
        url: INVOICES_URL,
        type: "POST",
        data: attachmentData,
        contentType: false,
        processData: false,
        headers: {Authorization: "Bearer " + localStorage.getItem("token")},
        success: (response) => {
            Swal.fire("Added!", response, "success");
            setTimeout(() => {
                window.location.href = "pagintaed.html"
            }, DELAY - 1500);
        },
        error: (err) => {
          console.log(err);
        },
      });
  }
  
    function handleAttachments(event) {
      const fileList = event.target.files;
  
      for (let i = 0; i < fileList.length; i++) {
        const attachment = fileList[i];
        attachments.push(attachment);
  
        const listItem = document.createElement('li');
        listItem.textContent = attachment.name;
        attachmentList.appendChild(listItem);
      }  
    }
  