import { zodResolver } from "@hookform/resolvers/zod"
import { Button, Stack, TextField, Typography } from "@mui/material"
import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { Navigate, useNavigate, useParams } from "react-router-dom"
import { z } from "zod"
import authorities from "../config/authorities"
import { useActiveUser } from "../contexts/activeUser"
import { getUserAuthorities } from "../services/authorityService"
import { useMyListEntryQuery } from "../services/hooks/myListEntryHook"
import {
  deleteMyListEntry,
  updateMyListEntry,
} from "../services/myListEntryService"

const myListEntrySchema = z.object({
  id: z.string(),
  title: z.string().min(2, "Title must be at least 2 characters"),
  text: z.string().min(2, "Text must be at least 2 characters"),
  importance: z.number().min(0, "Importance must be at least 0"),
  creation_date: z.string(),
  user_id: z.string(),
})

type MyListEntryFormValues = z.infer<typeof myListEntrySchema>

function MyListEntryEditPage() {
  const { myListEntryId } = useParams()
  const navigate = useNavigate()

  const { data: myListEntry, isLoading } = useMyListEntryQuery(
    myListEntryId ?? "",
    { enabled: !!myListEntryId }
  )
  const activeUser = useActiveUser()
  const userAuthorities = activeUser
    ? getUserAuthorities(activeUser)
    : new Set()

  const { handleSubmit, register, setValue } = useForm<MyListEntryFormValues>({
    resolver: zodResolver(myListEntrySchema),
    defaultValues: {
      id: "",
      title: "",
      text: "",
      importance: 1,
      creation_date: "",
      user_id: "",
    },
  })

  useEffect(() => {
    if (myListEntry) {
      setValue("id", myListEntry.id)
      setValue("title", myListEntry.title)
      setValue("text", myListEntry.text)
      setValue("importance", myListEntry.importance)
      setValue("creation_date", myListEntry.creation_date)
      setValue("user_id", myListEntry.user_id)
    }
  }, [myListEntry, setValue])

  if (!myListEntryId) {
    return <Navigate to="/mylistentries" replace />
  }

  const allowed =
    userAuthorities.has(authorities.MYLISTENTRY_MODIFY_ALL) ||
    (myListEntryId === activeUser?.id &&
      userAuthorities.has(authorities.MYLISTENTRY_MODIFY_OWN))

  if (!allowed) {
    return <Navigate to="/unauthorized" replace />
  }

  const onSubmit = async (data: MyListEntryFormValues) => {
    if (myListEntry) {
      try {
        await updateMyListEntry({ ...myListEntry, ...data })
        navigate("/mylistentries")
      } catch (error) {
        console.error("Failed to update myListEntry:", error)
      }
    }
  }

  const handleDelete = async () => {
    if (myListEntry) {
      try {
        await deleteMyListEntry(myListEntry.id)
        navigate("/mylistentries")
      } catch (error) {
        console.error("Failed to delete myListEntry:", error)
      }
    }
  }

  if (isLoading) {
    return <Typography>Loading...</Typography>
  }

  return (
    <Stack component={"form"} onSubmit={handleSubmit(onSubmit)} spacing={2}>
      <TextField
        label="Title"
        defaultValue={myListEntry?.title || ""}
        {...register("title")}
      />
      <TextField
        label="Text"
        defaultValue={myListEntry?.text || ""}
        {...register("text")}
      />
      <TextField
        label="Importance"
        type="number"
        defaultValue={myListEntry?.importance || 1}
        {...register("importance", { valueAsNumber: true })}
      />
      <Button type="submit" variant="contained" color="primary">
        Save
      </Button>
      <Button variant="contained" color="error" onClick={handleDelete}>
        Delete
      </Button>
    </Stack>
  )
}

export default MyListEntryEditPage
