import { zodResolver } from "@hookform/resolvers/zod"
import {
  Alert,
  Button,
  Container,
  Stack,
  TextField,
  Typography,
} from "@mui/material"
import { useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"
import { z } from "zod"
import { createMyListEntry } from "../services/myListEntryService"
import { MyListEntryCreate } from "../types/models/MyListEntryCreate.model"

const myListEntrySchema = z.object({
  title: z.string().min(2, "Title must be at least 2 characters"),
  text: z.string().min(2, "Text must be at least 2 characters"),
  importance: z.number().min(0, "Importance must be at least 0"),
})

type MyListEntryFormValues = z.infer<typeof myListEntrySchema>

function MyListEntryCreatePage() {
  const navigate = useNavigate()
  const [formError, setFormError] = useState<string>("")

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<MyListEntryFormValues>({
    resolver: zodResolver(myListEntrySchema),
    defaultValues: {
      title: "",
      text: "",
      importance: 1,
    },
  })

  const onSubmit = async (data: MyListEntryFormValues) => {
    setFormError("")
    try {
      const { title, text, importance } = data
      await createMyListEntry({
        title,
        text,
        importance,
      } as MyListEntryCreate)
      navigate("/mylistentries")
    } catch (error: unknown) {
      if (error instanceof Error) {
        setFormError(error.message || "Failed to create MyListEntry")
      } else {
        setFormError("Failed to create MyListEntry")
      }
    }
  }

  return (
    <Container maxWidth="sm">
      <Stack spacing={2} mt={4}>
        <Typography variant="h4" align="center">
          Create MyListEntry
        </Typography>

        {formError && <Alert severity="error">{formError}</Alert>}

        <form onSubmit={handleSubmit(onSubmit)}>
          <Stack spacing={2}>
            <TextField
              label="Title"
              type="text"
              fullWidth
              {...register("title")}
              error={!!errors.title}
              helperText={errors.title?.message}
            />
            <TextField
              label="Text"
              type="text"
              fullWidth
              {...register("text")}
              error={!!errors.text}
              helperText={errors.text?.message}
            />
            <TextField
              label="Importance"
              type="number"
              fullWidth
              {...register("importance", { valueAsNumber: true })}
              error={!!errors.importance}
              helperText={errors.importance?.message}
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
              Create
            </Button>
          </Stack>
        </form>
      </Stack>
    </Container>
  )
}

export default MyListEntryCreatePage
