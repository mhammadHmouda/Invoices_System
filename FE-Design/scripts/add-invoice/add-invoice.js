import { parseJwt} from "../../utils/tokenUtils.js"
import { INVOICES_URL, DELAY } from "../../settings/settings.js";
  
  
  const form = document.querySelector("#invoice-form");
  const formContainer = document.querySelector(".form-container");
  const addBtn = document.querySelector("#add-btn");
  const attachmentInput = document.getElementById('attachment');
  const attachmentList = document.getElementById('attachmentList');
  let attachments = [];

  attachmentInput.addEventListener('change', handleAttachments);

  const data = `<div class="row">
                    <div class="d-flex gap-1 justify-content-center align-items-center col-4">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="names[]" class="form-control" />
                    </div>
                    <div class="d-flex gap-1 justify-content-center align-items-center col-4">
                        <label for="unit">Unit Price</label>
                        <input type="text" id="unit" name="units[]" class="form-control" />
                    </div>
                    <div class="d-flex gap-1 justify-content-center align-items-center col-4">
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
  
  function onSubmit(e) {
    e.preventDefault();  
    const formData = new FormData(form);
    const units = formData.getAll("units[]");
    const quantities = formData.getAll("quantities[]");
    const itemNames = formData.getAll("names[]");
    const dataArray = units.map((unitPrice, index) => ({
      name: itemNames[index],  
      quantity: quantities[index],
      unitPrice,
    }));

    var total = 0;
    for (const item of dataArray) {
        const unitPrice = parseFloat(item.unitPrice);
        const quantity = parseInt(item.quantity);
        total += (unitPrice * quantity);
    }
  
    const { id } = parseJwt();

    const invoice = {
        totalPrice: total,
        user: { id: id },
        items: dataArray
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
  