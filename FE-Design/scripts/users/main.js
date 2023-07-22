import {
    DELAY,
    primaryColor,
    dangerColor,
    USERS_URL,
  } from "../../settings/settings.js";
  
  const token = localStorage.getItem("token");
  
  if (!token) {
    window.location.href = "login.html";
  }
  
  export function getUser(id) {
  
    return $.ajax({
      url: USERS_URL + `/${id}`,
      method: "GET",
      dataType: "json",
      headers: {Authorization: "Bearer " + localStorage.getItem("token")},
      success: function (data) {
      },
    });
  }
  
  export function deleteUser(id) {
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
          url: `${USERS_URL}/${id}`,
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
            console.error('Failed to delete user!', errorThrown);
          }
        });
      }
    });
  }
  
  export async function editUser(id) {
    const user = await getUser(id)
    const { value: data } = await Swal.fire({
      title: "Edit Role",
      html: `
        <select id="role" class="swal2-input w-75">
          <option value="SUPERUSER">SUPERUSER</option>
          <option value="USER">USER</option>
          <option value="AUDITOR">AUDITOR</option>
        </select>
      `,
      icon: "info",
      focusConfirm: false,
      showCancelButton: true,
      cancelButtonText: "Cancel",
      confirmButtonText: "Edit",
      confirmButtonColor: primaryColor,
      cancelButtonColor: dangerColor,
      preConfirm: () => {
        const role = user.role;
        user.role = $("#role").val();
        return {
          user: user,
          role: role
        };
      },
    });
  
    if (!data) return;
  
    const validStrings = ["SUPERUSER", "USER", "AUDITOR"];
    if (validStrings.includes(data.user.role) && data.user.role != data.role) {

        $.ajax({
            url: USERS_URL,
            type: 'PUT',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + localStorage.getItem('token'),
            },
            data: JSON.stringify(data.user),
            success: (responseText) => {
                Swal.fire("Edited!", responseText, "success");
                setTimeout(() => {
                location.reload();
                }, DELAY - 1500);
            },
            error: (jqXHR, textStatus, errorThrown) => {
              console.error('Failed to update user', errorThrown);
            }
          });
    }
    else{
        return Swal.fire("Error!", "Please change the user role!", "error");
    }
   
  }